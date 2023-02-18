(ns io.github.kit.gif2html.web.controllers.gifs
  (:require
   [ring.util.http-response :as http-response]))

(defn save-gif [{:keys [query-fn] :as opts} {{params :body} :parameters}]
  (query-fn :create-gif! params)
  (http-response/ok {:result :ok}))
