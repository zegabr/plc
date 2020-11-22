import Control.Concurrent
import Control.Concurrent.STM
import Control.Monad
import System.IO
import GHC.Conc.Sync


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

-- printRefil :: [Char] -> TMVar Int -> IO()
-- printRefil juice value = do
--     putStrLn $ "O refrigerante "++ juice ++" foi reabastecido com 1000 ml, e agora possui "++ (show value) ++" ml" -- ERROR HERE

-- printClient :: [Char] -> Int -> IO()
-- printClient juice index = do
--     putStrLn $ "O cliente "++(show index)++" do refrigerante "++ juice ++" está enchendo seu copo"

tryRefil :: [Char] -> TMVar Int -> STM()
tryRefil j value = do
    currentValue <- takeTMVar value
    if currentValue < 1000 then do
        putTMVar value (currentValue + 1000)
        -- threadDelay 1500000 -- 1.5 sec
        -- unsafeIOToSTM(printRefil j value)
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
        -- threadDelay 1000000 -- 1 sec
        -- unsafeIOToSTM(printClient j i)
        
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
    -- initial stock
    coca    <- atomically (newTMVar 2000)
    polo    <- atomically (newTMVar 2000)
    quate   <- atomically (newTMVar 2000)

    -- pegar numCocaClients numPoloClients numQuateClients
    numCocaClients <- getLine
    numPoloClients <- getLine
    numQuateClients <- getLine

    let cocanum = 5
    let polonum = 5
    let quatenum = 5
    
    aliveThreads <- atomically (newTMVar (cocanum + polonum + quatenum))
    lockMachine <- atomically(newTMVar 1)

    forkIO $ atomically $ producer coca polo quate lockMachine
    mapM_(\x -> forkIO $ atomically $ consumer "COCA" x coca polo quate aliveThreads lockMachine) [1..cocanum]
    mapM_(\x -> forkIO $ atomically $ consumer "POLO" x coca polo quate aliveThreads lockMachine) [1..polonum]
    mapM_(\x -> forkIO $ atomically $ consumer "QUATE" x coca polo quate aliveThreads lockMachine) [1..quatenum]
    atomically(waitThreads aliveThreads)
    return ()