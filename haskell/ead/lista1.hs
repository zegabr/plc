expNE :: Float -> Int -> Float
expNE a 1 = a
expNE a 0 = 1
expNE a b = a * (expNE a (b-1))

-- ---------------------
mdc :: Int -> Int -> Int
mdc a b
    |b == 0 = a
    |b < 0  = mdc a (-b)
    |otherwise = mdc b (mod a b)

-- ----------------------
numDiv :: Int -> Int -> Int
numDiv a b
    |b==0               = 0
    |(mod a b) /= 0     = 0
    |otherwise          = 1 + (numDiv (div a b) b)

--------------------------------
contar :: Int -> [Int] -> Int
contar num [] = 0
contar num (x:xs)
    |num==x     = 1 + contar num xs
    |otherwise  =  contar num xs

unicos :: [Int] -> [Int]
unicos lista = [a | a <- lista, (contar a lista) == 1]

-----------------------------------

getAlternated :: Bool -> [a] -> [a]
getAlternated _ [] = []
getAlternated get (x:xs)
    |get        = x:getAlternated (not get) xs
    |otherwise  = getAlternated (not get) xs

halve :: [a] -> ([a],[a])
halve [] = ([],[])
halve lista = (getAlternated True lista , getAlternated False lista)

---------------------------------------

getFirst :: Int -> [a] -> [a]
getFirst _ [] = []
getFirst n (x:xs)
    |n <= 0 = []
    |otherwise  = x : getFirst (n-1) xs

getFromPosition :: Int -> [a] -> [a]
getFromPosition _ [] = []
getFromPosition n (x:xs)
    |n<=0   = []
    |n==1   =  x:xs
    |otherwise = getFromPosition (n-1) xs

remDiv :: Int -> [a] -> ([a],[a])
remDiv n lista = (getFirst (n-1) lista, getFromPosition (n+1) lista)