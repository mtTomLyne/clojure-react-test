import React, {useState, useEffect} from "react";
import Item from "./Item"

export default function ItemList(props) {

    // this is a component that controls the list of doctors onscreen

    // array state of which doctors are expanded
    const [expanded, setExpanded] = useState([]);

    // helper method to reset the array to all 0s
    const resetArray = () => {
        var newArray = [];
        for (var count = 0; count < props.data.length; count+=1)
        {
            var newArray = newArray.concat(false);            
        }
        setExpanded(newArray);
    }

    // reset the array whenever new data comes in! :D
    useEffect(() =>
    {
        resetArray();
    }, props.data);

    // handler method for when a doctor item gets clicked, and needs expanding
    // this method is owned by the "item list", but is provided to each child component "item" (see later)
    const onDoctorExpanded = (number) => 
    {
        var array = expanded;
        const wasExpanded = array[number];
        var newArray = [];
        for (var count = 0; count < props.data.length; count+=1)
        {
            var newArray = newArray.concat(false);            
        }

        // set the clicked item's expanded state to the appropriate new state
        newArray[number] = !wasExpanded;
        setExpanded(newArray);
    }

// map the list of doctors to new "Item" components. we pass in the itemId, the handler method for "hey, i'm an item and i need expanding!", and the state of the expanded array
    return (
        <ul class="list">
            {props.data.map((item, index) => <Item itemId={index} expanded={expanded} onExpanded={onDoctorExpanded} {...item}/>)}
        </ul>
    );
}
