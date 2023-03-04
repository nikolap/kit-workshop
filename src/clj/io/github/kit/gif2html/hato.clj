(ns io.github.kit.gif2html.hato
  (:require
    [integrant.core :as ig]
    [hato.client :as hc]))

(defmethod ig/init-key :http/hato [_ opts]
  (hc/build-http-client opts))
