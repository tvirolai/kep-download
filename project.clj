(defproject kep-dl "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [clj-http "3.7.0"]
                 [enlive "1.1.6"]]
  :main ^:skip-aot kep-dl.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
