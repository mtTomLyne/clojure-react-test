import React from "react";
import ReactDOM from "react-dom";
//import './index.css';
//import App from './App';
//import reportWebVitals from './reportWebVitals';
import axios from "axios";
import "@babel/polyfill";

export const testData = [];

export class Item extends React.Component {
  render() {
    const result = this.props;
    return (
      <span>
        <span>Input: {result.input}</span> &ensp; &ensp;
        <span>Output: {result.output}</span>
      </span>
    );
  }
}

export const ItemList = (props) => (
  <div>
    {props.results.map((result) => (
      <Item {...result} />
    ))}
  </div>
);

export class Form extends React.Component {
  constructor(props) {
    super(props);
    this.state = { number: "" };
  }

  handleSubmit = async (event) => {
    event.preventDefault();
    const resp = await axios.get(
      `http://localhost:8080/square?number=${this.state.number}`
    );
    this.props.onSubmit(resp.data);
  };

  render() {
    return (
      <form onSubmit={this.handleSubmit}>
        <input
          type="text"
          value={this.state.number}
          onChange={(event) => this.setState({ number: event.target.value })}
          required
        />
        <button>Square this number!</button>
      </form>
    );
  }
}

export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      results: testData,
    };
  }

  addNewResult = (resultData) => {
    console.log("App", resultData);
    this.setState((prevState) => ({
      results: [...prevState.results, resultData],
    }));
  };

  render() {
    return (
      <>
        <Form onSubmit={this.addNewResult} />
        <ItemList results={this.state.results} />
       </>
    );
  }
}

//
//if (typeof window !== 'undefined') {

//React.render(
//  <App title="Test App That Squares Numbers Or Something" />,
//  document.getElementById("mountNode"),
//);

//}

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
//reportWebVitals();
