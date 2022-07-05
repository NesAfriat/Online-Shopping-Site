import classes from "../item.module.css";
import Card from "../Card"
import { Link } from "react-router-dom";
function ShopItem(props){
    return <li className={classes.item}>
        <Card>
        <div className={classes.content}>
        <h2>
        {props.name} 
        </h2>
        <h3>founded by {props.founder}</h3>
        <address>
        {props.adress}
        </address>
        <p>{props.description}</p>
        </div>
        <div className={classes.actions}>
        <Link to={`/all-shops/${props.id}`} >Enter shop</Link>
        </div>
        </Card>
    </li>
   }
   
   export default ShopItem;