(ns task2-test
  (:require [clojure.test :refer :all])
  (:use task2))

(deftest primes-test
  (is (= (nth primes 0) 2))
  (is (= (nth primes 10) 31))
  (is (= (nth primes 100) 547))
  (is (= (nth primes 1000) 7927))
  (is (= (nth primes 10000) 104743)))