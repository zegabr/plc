------A------------
data Prop = Var Char | Const Bool | Not Prop | And Prop Prop | Or Prop Prop | Implies Prop Prop | Iff Prop Prop deriving (Eq,Show,Read)
---------B--------------
type Subst = [(Char, Bool)]
---------C---------------
getVal :: Subst -> Prop -> Bool
getVal [] (Var a) = False -- never executes this line (hopefully)
getVal ((char, bool): xs) (Var a)
  | char == a   = bool
  | otherwise = getVal xs (Var a)

eval :: Subst -> Prop -> Bool
eval table (Var a) = getVal table (Var a)
eval _ (Const b) = b
eval table (Not exp) = not (eval table exp)
eval table (And a b) = (eval table a) && (eval table b)
eval table (Or a b) = (eval table a) || (eval table b)
eval table (Implies a b) = (not (eval table a)) || (eval table b)
eval table (Iff a b) = (eval table (Implies a b)) && (eval table (Implies b a))
-----------------------D------------------
varsHelper :: Prop -> [Char] -> [Char] -- vars helper
varsHelper (Var c) res = c:res
varsHelper (Const b) res = res
varsHelper (Not exp) res = varsHelper exp res
varsHelper (And a b) res = varsHelper a (varsHelper b res)
varsHelper (Or a b)  res = varsHelper a (varsHelper b res)
varsHelper (Implies a b) res = varsHelper a (varsHelper b res)
varsHelper (Iff a b)  res = varsHelper a (varsHelper b res)

vars :: Prop -> [Char]
vars p = varsHelper p []
------------------------------E---------------------
addBool :: Bool -> [[Bool]] -> [[Bool]] -- add given bool to every [Bool] in list
addBool b [] = []
addBool b (x:xs) = (b:x) : (addBool b xs)

boolsHelper :: Int -> [[Bool]] -> [[Bool]]
boolsHelper 0 _ = []
boolsHelper 1 [] = [[True],[False]]
boolsHelper n res = (addBool False (boolsHelper (n-1) res ) ) ++ (addBool True (boolsHelper (n-1) res ) )

bools :: Int -> [[Bool]]
bools n = boolsHelper n []
---------------------------F------------------------------
existsIn :: Char -> [Char] -> Bool -- check if char exists in list
existsIn c [] = False
existsIn c (x:xs)
  | c == x = True
  | otherwise = existsIn c xs

insertUnique :: Char -> [Char] -> [Char] -- insert char in list if it isn't there
insertUnique c res
  | existsIn c res == True = res
  | otherwise = c:res

removeDuplicates :: [Char] -> [Char] -> [Char] -- remove duplicates from (d)
removeDuplicates [] res = res
removeDuplicates (x:xs) res = removeDuplicates xs (insertUnique x res)


getZips :: [Char] -> [[Bool]] -> [Subst] -> [Subst]
getZips uv [] res = res
getZips uv (lb:xs) res = (getZips uv xs ((zip uv lb):res))

substs :: Prop -> [Subst]
substs p = getZips uniqueVars listOfBools []
    where 
        uniqueVars = removeDuplicates (vars p) []
        listOfBools = bools (length uniqueVars)
---------------------G----------------------------
isTautHelper :: [Subst] -> Prop -> Bool
isTautHelper [] p = True
isTautHelper (sub:xs) p
  | (eval sub p) == False = False
  | otherwise = isTautHelper xs p

isTaut :: Prop -> Bool
isTaut p = isTautHelper (substs p) p


