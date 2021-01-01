import React from "react";
import { Route } from "react-router-dom";
import LayoutComponent from './components/LayoutComponent';

function App() {
  return (
    <div className="App" style={{ height: "100%" }}>
      <Route exact path="/" component={LayoutComponent} />
    </div>
  );
}

export default App;
