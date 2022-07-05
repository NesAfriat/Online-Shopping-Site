import classes from "../../styling/item.module.css";
import Bid from "../BasicComponent/Bid";

const ViewMyBids = (props) => {
    console.log(props.bids)

    return (
        <div className={classes.content}>
            <h2>My bids:</h2>
            <ul >
                {props.bids.map((bid, i) => {
                    return (
                        <li key={i}>
                            <Bid
                                visitorId={props.visitorId}
                                approvers={bid.approvers}
                                notApprovedYet={bid.notApprovedYet}
                                id={bid.id}
                                price={bid.price}
                                product={bid.product}
                                reloadBids={props.reloadBids}
                                amount={bid.quantity}
                            />
                        </li>
                    )
                })}
            </ul>
        </div>
    )
}

export default ViewMyBids