import {useState, useEffect} from "react"

// this points to the clojure api
const baseUrl = "http://localhost:8080/doctors";

// helper method to make api calls. if an error occurs, it throws the error to be caught in the error boundary
export default function useFetch(url) {
    const [data, setData] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => 
    {
      setLoading(true);
      async function init() 
      {
        try 
        {
          const response = await fetch(baseUrl);
          if (response.ok)
          {
              const json = await response.json();
              setData(json);
          } else 
          {
              throw response;
          }
        }
        catch (e) 
        {
          setError(e)
        } finally 
        {
          setLoading(false)
        }
      }
      init();
    }, [url])

    return {data, error, loading};
}