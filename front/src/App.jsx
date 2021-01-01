import React from "react";
import { Route } from "react-router-dom";
import MainComponent from "./components/MainComponent";

function App() {
  return (
    <div className="App">
      <Route exact path="/" component={MainComponent} />
    </div>
  );
}

export default App;
