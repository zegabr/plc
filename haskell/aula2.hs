--guarda
{-
 -nomefuncao x
| guarda1 = exp1
| guarda2 = exp2
...
| guardaN = expN
 -
 - -}

x=2
maiorInt :: Int -> Int -> Int
maiorInt x y 
 | x>=y=x
 |otherwise=y

quadrado :: Int -> Int
quadrado x=x*x

--funcoes recursivas

vendas :: Int -> Int
vendas 0 = 5
vendas 1 = 6
vendas 2 = 9
vendas 3 = 2

totalvendas :: Int -> Int
totalvendas n
 | n==0=vendas 0
 | n==1=vendas 1
 | otherwise=100


fibo :: Int -> Int
fibo n
 |n==0||n==1 = 1
 |otherwise = fibo (n-1) + fibo (n-2)

--define
true=True
false=False
