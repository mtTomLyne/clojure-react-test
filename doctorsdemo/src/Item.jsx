import React, {useState} from "react";
import { useEffect } from "react";
import { property } from "underscore";


export default function Item(props) {

    const imageUrl = "/images/" + props.image;
    // hover state - whether the mouse is currently hovering over the current item
    const [hover, setHover] = useState(false)

    return (
        <li 
              class="itemInList"
              style=
              {{
                  // work out which color to draw the doctor with depending on the "hover" state
                  backgroundColor: (hover ? "#99CCFF" : (props.expanded[props.itemId] ? "#DDEEFF" : "white"))
              }} 

              // when the doctor is clicked, invoke the parent component method. we need to tell the parent "which" item has been clicked!
              onClick={event => props.onExpanded(props.itemId)} 

              // event handlers for hovering
              onMouseEnter={event => setHover(true)}
              onMouseLeave={event => setHover(false)} >

                
            <header >
                <nav>
                    <ul>
                        <p />
                        <img src={imageUrl}/> 
                        <li>
                            <div>
                                <h1>{props.name}</h1>
                            </div>
                        
                        {/* Render only part of the doctor description if the current doctor hasnt been expanded */}
                        {(!props.expanded[props.itemId]) && 
                            <li>
                                <div>{(props.description.length < 200) ? props.description : (props.description.substring(0, 200) + "....")}</div>
                            </li>
                        }
                        
                        {/* If expanded, display the whole thing */}
                        {(props.expanded[props.itemId]) &&
                            <li>
                              <div>{props.description}</div>
                            </li>
                        }
                        </li>
                    </ul>
                </nav>
            </header>
        </li>
    );
}
