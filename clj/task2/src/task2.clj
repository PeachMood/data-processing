(ns task2)

(defn multiples? [p]
  (fn [x] (zero? (mod p x))))

(def primes
  (lazy-seq (filter
              (fn [p]
                (not-any?
                  (multiples? p)
                  (take-while #(<= (* % %) p) primes)))
              (drop 2 (range)))))