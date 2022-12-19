(ns task3-test
  (:require [clojure.test :refer :all])
  (:use task3))

(defn heavy-even? [n]
  (Thread/sleep 10)
  (even? n))

(deftest pfilter-test
  (is (= (filter odd? (range 0)) (pfilter odd? (range 0))))
  (is (= (filter even? (range 100)) (pfilter even? (range 100))))
  (is (= (filter even? (range 1000)) (pfilter even? (range 1000)))))

(deftest pfilter-time-test
  (time (doall (take 100 (filter even? (iterate inc 0)))))
  (time (doall (take 100 (pfilter even? (iterate inc 0))))))
