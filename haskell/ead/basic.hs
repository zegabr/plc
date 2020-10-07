{- #########################################################

        FirstScript.hs
        Simon Thompson, August 2010.

######################################################### -}
module Basic where

--      The value size is an integer (Integer), defined to be 
--      the sum of twelve and thirteen.

size :: Integer
size = 12+13

--      The function to square an integer.

square :: Integer -> Integer
square n = n*n

--      The function to double an integer.
        
double :: Integer -> Integer
double n = 2*n

--      An example using double, square and size.
         
example :: Integer
example = double (size - square (2+2))

{-
#################################3
    testing by jgsp2
################################
-}

eq3 :: Integer -> Integer -> Integer -> Bool
eq3 a b c = (a == b) && (b == c)

dif3 :: Integer -> Integer -> Integer -> Bool
dif3 a b c = (a /= b) && (b /= c) && (a /= c)

-- EXERCISES
