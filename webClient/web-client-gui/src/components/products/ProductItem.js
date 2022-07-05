import classes from "../item.module.css";
import Card from "../Card"

function ShopItem(props){
    return <li className={classes.item}>
        <Card>
        <div className={classes.content}>
        <h2>
        {props.name} 
        </h2>
        <h3>Category: {props.category}</h3>
        <h3>Price: {props.price}</h3>
        <h3>Amount: {props.qunatity}</h3>
        <h3>Product ID: {props.id}</h3>
        <p>{props.description}</p>
        </div>
        </Card>
    </li>
   }
   
   export default ShopItem;