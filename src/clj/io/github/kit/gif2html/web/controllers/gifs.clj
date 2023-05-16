(ns io.github.kit.gif2html.web.controllers.gifs
  (:require
   [gif-to-html.convert :as convert]
   [hato.client :as hato]
   [ring.util.http-response :as http-response]
   [clojure.tools.logging :as log]))

(def Gif
  [:map
   [:id integer?]
   [:ascii map?]
   [:name string?]
   [:created_at inst?]])

(defn save-gif [{:keys [query-fn http-client] :as opts} {{params :body} :parameters}]
  (try
    (->> (hato/get
           (:link params)
           {:http-client http-client
            :as          :stream})
         :body
         (convert/gif->html)
         (assoc params :ascii)
         (query-fn :create-gif!)
         (first)
         (http-response/ok))
    (catch Exception e
      (log/error e "failed to save the GIF")
      (http-response/internal-server-error))))

(defn list-gifs [{:keys [query-fn] :as opts} _]
  (http-response/ok (query-fn :list-gifs {})))

(defn get-gif-by-id [{:keys [query-fn] :as opts} {{params :path} :parameters}]
  (http-response/ok (query-fn :get-gif-by-id params)))

(comment
  (save-gif (user/api-ctx) {:parameters {:body {:link "https://media.tenor.com/JMzBeLgNaSoAAAAj/banana-dance.gif" :name "foo"}}})
  
  (->> (list-gifs (user/api-ctx) nil)
    
       :body
       vector?
       #_seq?
      #_(map #(select-keys % [:name :id])))
  
  (get-gif-by-id (user/api-ctx) {:parameters {:path {:id 3}}})
  )