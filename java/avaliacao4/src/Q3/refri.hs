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
waitThreads :: MVar Int -> IO ()
waitThreads fim = do
    f <- takeMVar fim
    if (f > 0) then do
        putMVar fim f
        waitThreads fim
    else
        return ()

printRefil :: [Char] -> MVar Int -> IO()
printRefil juice value = do
    putStrLn $ "O refrigerante "++ juice ++" foi reabastecido com 1000 ml, e agora possui "++ (show value) ++" ml"

printClient :: [Char] -> Int -> IO()
printClient juice index = do
    putStrLn $ "O cliente "++(show index)++" do refrigerante "++ juice ++" está enchendo seu copo"

tryRefil :: [Char] -> MVar Int -> IO()
tryRefil j value = do
    currentValue <- readMVar value
    if currentValue < 1000 then
        putMVar value (currentValue + 1000)
        --sleep 1500ms
        printRefil j value
    else
        return()

producer :: MVar Int -> MVar Int -> MVar Int -> MVar Int -> IO()
producer coca polo quate machine = do
    ------- check coca
    usingMachine <- takeMVar machine
    tryRefil "COCA" coca
    putMVar machine usingMachine
    ------------ check polo
    usingMachine <- takeMVar machine
    tryRefil "POLO" polo
    putMVar machine usingMachine
    ------------- check quate
    usingMachine <- takeMVar machine
    tryRefil "QUATE" quate
    putMVar machine usingMachine
    -- call again infinitelly
    producer coca polo quate machine


tryFill :: [Char] -> Int -> MVar Int -> MVar Int -> MVar Int -> IO ()
tryFill j i value aliveThreads machine = do
    currentValue <- readMVar value
    if currentValue >= 300 then do
        usingMachine <- takeMVar machine
        putMVar value (currentValue - 300)
        -- sleep 1000ms
        printClient j i
        
        currentAliveThreads <- readMVar aliveThreads
        putMVar aliveThreads (currentAliveThreads-1)
        
        putMVar machine usingMachine
    else 
        tryFill j i value aliveThreads machine
    


consumer :: [Char] -> Int-> MVar Int -> MVar Int -> MVar Int -> MVar Int -> MVar Int -> IO()
consumer "COCA" index coca polo quate aliveThreads machine = tryFill "COCA" index coca aliveThreads machine
consumer "POLO" index coca polo quate aliveThreads machine = tryFill "POLO" index polo aliveThreads machine
consumer "QUATE" index coca polo quate aliveThreads machine = tryFill "QUATE" index quate aliveThreads machine


main :: IO ()
main = do
    hSetBuffering stdout NoBuffering
    -- initial stock
    coca    <- newMVar 2000
    polo    <- newMVar 2000
    quate   <- newMVar 2000

    -- pegar numCocaClients numPoloClients numQuateClients
    let numCocaClients = 2
    let numPoloClients = 1
    let numQuateClients = 0
    lockMachine <- newMVar 1
    
    aliveThreads <- newMVar (numCocaClients + numPoloClients + numQuateClients)

    forkIO(producer coca polo quate lockMachine)
    forkIO(consumer "COCA" 1 coca polo quate aliveThreads lockMachine)
    forkIO(consumer "COCA" 2 coca polo quate aliveThreads lockMachine)
    forkIO(consumer "POLO" 1 coca polo quate aliveThreads lockMachine)
    waitThreads aliveThreads
    return ()