import classes from "../../styling/item.module.css";
import {useState} from "react";
import PurchaseBid from "../MyBids/PurchaseBid";

const Bid = (props) => {
    const [purchase, setPurchase] = useState(false);

    return (
        <div className={classes.content}>
            <h1>Bid id: {props.id}</h1>
            <h2>for product: {props.product.productName}</h2>
            <h3>original price: {props.product.price}</h3>
            <h3>asked price: {props.price}</h3>
            <h3>amount: {props.amount}</h3>
            {props.notApprovedYet.length !== 0 ? (
                <>
                    <h4>waiting approval from: </h4>
                    <ul>
                        {props.notApprovedYet.map((name, i) => {
                            return (
                                <li key={i}>
                                    <p>{name}</p>
                                </li>
                            )
                        })}
                    </ul>
                </>
            ) : (
                <>
                    <h4>Bid has been approved!</h4>
                    <button onClick={()=>setPurchase(true)}>purchase</button>
                </>)}
            {purchase && <PurchaseBid visitorId={props.visitorId} bidId={props.id} setter={setPurchase} reloadBids={props.reloadBids}/>}
        </div>
    )
}

export default Bid