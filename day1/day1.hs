{-# LANGUAGE OverloadedStrings #-}

import Data.Char (digitToInt, isDigit)
import Data.Text (Text, pack, replace, unpack)
import Distribution.Compat.CharParsing (digit)
import System.IO ()

sanitize :: String -> String
sanitize input = unpack (replace "nine" "ni9ne" $ replace "eight" "ei8ght" $ replace "seven" "se7ven" $ replace "six" "si6x" $ replace "five" "fi5ve" $ replace "four" "fo4ur" $ replace "three" "th3ree" $ replace "two" "tw2o" $ replace "one" "on1e" (pack input))

toListsOfNumbers :: [String] -> [[Int]]
toListsOfNumbers = map (map digitToInt . filter isDigit)

calculateCalibrationValue :: [[Int]] -> [Int]
calculateCalibrationValue = map (\numbers -> read (show (head numbers) ++ show (last numbers)))

main :: IO ()
main = do
  contents <- readFile "input.txt"

  let listsOfNumbers = toListsOfNumbers $ lines contents
  let output1 = sum (calculateCalibrationValue listsOfNumbers)
  print output1

  let sanitizedListsOfNumbers = toListsOfNumbers $ map sanitize $ lines contents
  let output2 = sum (calculateCalibrationValue sanitizedListsOfNumbers)
  print output2