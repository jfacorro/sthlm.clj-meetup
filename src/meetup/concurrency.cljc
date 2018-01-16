(ns meetup.concurrency)

(def n 1000)
(def x (atom 0))

#?(:clje
   (defn spawn [f & args]
     (erlang/spawn :clj_rt :apply #erl(f args))))

(defn sleep-swap! [x f]
  (#?(:clj Thread/sleep :clje timer/sleep) (rand-int 1000))
  (swap! x f))

(println "Spawning" n "processes")

(dotimes [i n]
  #?(:clj
     (future (sleep-swap! x inc))
     :clje
     (spawn sleep-swap! x inc)))

(println "Finished spawning" n "processes")

(loop []
  (when (< @x n)
    (println "Value:" @x)
    (#?(:clj Thread/sleep :clje timer/sleep) 100)
    (recur)))

(println "Final value:" @x)

;; Avoid waiting forever for threads to stop.
#?(:clj (shutdown-agents))
