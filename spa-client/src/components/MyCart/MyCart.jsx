import {useState} from "react";
import PurchaseCart from "./PurchaseCart";
import classes from "../../styling/item.module.css";
import ViewCartProducts from "./ViewCartProducts";
import CartProduct from "../BasicComponent/CartProduct";

const MyCart = (props) => {
    const [purchase, setPurchase] = useState(false);

    const purchaseHandler = (e) => {
        if (Object.entries(props.cart.baskets).filter(([shopId, basket])=>{
            return Object.entries(basket.products).length > 0}
        ).length > 0){
            setPurchase(true)
        } else{
            alert("No products in cart")
            setPurchase(false)
        }


    }
    return (
        <div className={classes.content2}>
            {purchase === false ? (
                <div>
                    <ViewCartProducts visitorId={props.visitorId} cart={props.cart} setter={props.setter}
                                      reloadCart={props.reloadCart}/>
                    <button onClick={purchaseHandler}>purchase cart</button>
                </div>
            ) : (
                <PurchaseCart visitorId={props.visitorId} setter={setPurchase} reloadCart={props.reloadCart}/>
            )}
        </div>)
};
export default MyCart;

