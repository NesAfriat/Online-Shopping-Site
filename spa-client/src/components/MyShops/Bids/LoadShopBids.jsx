import classes from "../../../styling/item.module.css";
import ShopBid from "../../BasicComponent/ShopBid";

const LoadShopBids = (props) => {

    return(
        <div className={classes.content}>
            <h2>shop bids:</h2>
            <ul >
                {props.bids.map((bid, i) => {
                    return (
                        <li key={i}>
                            <ShopBid
                                visitorId={props.visitorId}
                                approvers={bid.approvers}
                                notApprovedYet={bid.notApprovedYet}
                                id={bid.id}
                                price={bid.price}
                                product={bid.product}
                            />
                        </li>
                    )
                })}
            </ul>
        </div>
    )
}
export default LoadShopBids