import React from "react";

export default class ErrorBoundary extends React.Component {
    constructor(props) {
        super(props);
        this.state = { hasError: false };
    }
  
    static getDerivedStateFromError(error) {
        return { hasError: true };  
    }

    render() 
    {
        if (this.state.hasError) {
          // If an error occured, render the "error" text before the rest of the document
          return [<h1 style={{backgroundColor: "#FF9999"}}>Something went wrong. Please refresh the page and try again.</h1>, this.props.children];
        }

        return this.props.children; 
    }
}