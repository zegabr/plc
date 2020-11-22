import Control.Concurrent
import Control.Concurrent.MVar
import Control.Concurrent.STM
import Control.Monad
import System.IO

-- 1 maquina (o lock), contem pcoca 2k, polo 2k e quate 2k (ok)
-- N clientes do tipo PCOCA | POLO | QUATE 
    -- cada cliente leva 1000ms pra preencher o copo com 300ml (usando a maquina, se tiver refri suficiente)
        -- O cliente N do refrigerante X está enchendo seu copo
-- o produtor demora 1500ms pra dar refil (de 1000ml) na maquina qnd algum refri tem menos de 1000ml
    --O refrigerante X foi reabastecido com 1000 ml, e agora possui Y ml

-- se fim chega a 0 ,acabou as threads



-- join (will keep main from stopping)
waitThreads :: TMVar Int -> STM ()
waitThreads fim = do
    f <- takeTMVar fim
    if (f > 0) then do
        putTMVar fim f
        waitThreads fim
    else
        return ()

printRefil :: [Char] -> TMVar Int -> IO()
printRefil juice value = do
    putStrLn $ "O refrigerante "++ juice ++" foi reabastecido com 1000 ml, e agora possui "++ (show value) ++" ml"

printClient :: [Char] -> Int -> IO()
printClient juice index = do
    putStrLn $ "O cliente "++(show index)++" do refrigerante "++ juice ++" está enchendo seu copo"

tryRefil :: [Char] -> TMVar Int -> STM()
tryRefil j value = do
    currentValue <- takeTMVar value
    if currentValue < 1000 then
        putTMVar value (currentValue + 1000)
        --sleep 1500ms
        atomically(printRefil j value)
    else
        return()

producer :: TMVar Int -> TMVar Int -> TMVar Int -> TMVar Int -> STM()
producer coca polo quate machine = do
    ------- check coca
    usingMachine <- takeTMVar machine
    tryRefil "COCA" coca
    putTMVar machine usingMachine
    ------------ check polo
    usingMachine <- takeTMVar machine
    tryRefil "POLO" polo
    putTMVar machine usingMachine
    ------------- check quate
    usingMachine <- takeTMVar machine
    tryRefil "QUATE" quate
    putTMVar machine usingMachine
    -- call again infinitelly
    producer coca polo quate machine


tryFill :: [Char] -> Int -> TMVar Int -> TMVar Int -> TMVar Int -> STM ()
tryFill j i value aliveThreads machine = do
    currentValue <- readTMVar value
    if currentValue >= 300 then do
        usingMachine <- takeTMVar machine
        putTMVar value (currentValue - 300)
        -- sleep 1000ms
        atomically(printClient j i)
        
        currentAliveThreads <- readTMVar aliveThreads
        putTMVar aliveThreads (currentAliveThreads-1)
        
        putTMVar machine usingMachine
    else 
        retry
    


consumer :: [Char] -> Int-> TMVar Int -> TMVar Int -> TMVar Int -> TMVar Int -> TMVar Int -> STM()
consumer "COCA" index coca polo quate aliveThreads machine = tryFill "COCA" index coca aliveThreads machine
consumer "POLO" index coca polo quate aliveThreads machine = tryFill "POLO" index polo aliveThreads machine
consumer "QUATE" index coca polo quate aliveThreads machine = tryFill "QUATE" index quate aliveThreads machine


main :: IO ()
main = do
    hSetBuffering stdout NoBuffering
    -- initial stock
    coca    <- atomically (newTMVar 2000)
    polo    <- atomically (newTMVar 2000)
    quate   <- atomically (newTMVar 2000)

    -- pegar numCocaClients numPoloClients numQuateClients
    let numCocaClients = 2
    let numPoloClients = 1
    let numQuateClients = 0
    lockMachine <- atomically(newTMVar 1)
    
    aliveThreads <- atomically (newTMVar (numCocaClients + numPoloClients + numQuateClients))

    forkIO(atomically(producer coca polo quate lockMachine))
    forkIO(atomically(consumer "COCA" 1 coca polo quate aliveThreads lockMachine))
    forkIO(atomically(consumer "COCA" 2 coca polo quate aliveThreads lockMachine))
    forkIO(atomically(consumer "POLO" 1 coca polo quate aliveThreads lockMachine))
    atomically(waitThreads aliveThreads)
    return ()