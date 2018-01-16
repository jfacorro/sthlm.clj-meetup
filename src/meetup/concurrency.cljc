(ns meetup.concurrency)

(def n 1000)
(def x (atom 0))

#?(:clje
   (defn spawn [f & args]
     (erlang/spawn :clj_rt :apply #erl(f args))))

(defn swap-inc [x]
  #?(:clj
     (Thread/sleep (rand-int 1000))
     :clje
     (timer/sleep (rand-int 1000)))
  (swap! x inc))

(dotimes [i n]
  #?(:clj
     (future (swap-inc x))
     :clje
     (spawn swap-inc x)))

(println "Finished spawning" n "processes")

(loop []
  (when (< @x n)
    (println "Value:" @x)
    #?(:clj
       (Thread/sleep 100)
       :clje
       (timer/sleep 100))
    (recur)))

(println "Final value:" @x)

;; Avoid waiting forever for threads to stop.
#?(:clj (shutdown-agents))
