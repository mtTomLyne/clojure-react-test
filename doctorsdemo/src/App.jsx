import React, {useState, useEffect} from "react";
import "./App.css";
import Footer from "./Footer";
import Header from "./Header";
import Spinner from "./Spinner"
import useFetch from "./services/useFetch"
import ItemList from "./ItemList"


export default function App() {
  
  // speciality state - which speciality the user wants to filter on
  const [spec, setSpec] = useState("");

  // helper to define some states for the app lifetime
  const {data: doctors, loading, error} = useFetch(spec);

  // work out which doctors to display in realtime
  const filteredDoctors = spec ? doctors.filter((p) => p.specialityName === (spec)) : doctors;

  if (error) throw error;
  
  // helper to render the filter dropdown
  const renderFilter = 
  (
      <section id="filters">
          <label htmlFor="size">Filter by Specialist Area:</label>{" "}
              <select id="size" value={spec} onChange={(e) => setSpec(e.target.value)}>
                  <option value="">All</option>
                  <option value="Pediatrician">Pediatrician</option>
                  <option value="Neurologist">Neurologist</option>
                  <option value="Psychiatrist">Psychiatrist</option>
              </select>
          {spec && <h2>Found {filteredDoctors.length} doctor{filteredDoctors.length == 1 ? "" : "s"}</h2>}
      </section>
  )
  
  // if we are loading, render the spinner instead of the doctors
  if (loading) return (
    <>
        <div className="content">
            <Header />
            <main>
                {renderFilter}
                <Spinner />
            </main>
        </div>
        <Footer />
    </>
  )

  return (
    <>
        <div className="content">
            <Header />
            <main>
                {renderFilter}
                <section id="doctors">
                    <ItemList data={filteredDoctors} />
                </section>
            </main>
        </div>
        <Footer />
    </>
  );
}
