(ns task1)

(defn is-correct-word [word]
  (not= (get word (- (count word) 1)) (get word (- (count word) 2))))

(defn concat-letter [words letter]
  (map (fn [word] (str word letter)) words))

(defn extend-words [words alphabet]
  (filter
    is-correct-word
    (reduce
      (fn [extended-words letter] (concat extended-words (concat-letter words letter)))
      '()
      alphabet)))

(defn create-language [length alphabet]
  (if (> length 0)
    (reduce
      (fn [words, _] (extend-words words alphabet)) alphabet (range (- length 1)))))