import classes from "../../styling/item.module.css";

const ShopBid = (props) => {

    return (
        <div className={classes.content}>
            <h1>Bid id: {props.id}</h1>
            <h2>for product: {props.product.productName}</h2>
            <h3>original price: {props.product.price}</h3>
            <h3>asked price: {props.price}</h3>
            {props.approvers.length !== 0 ? (
                <>
                    <h4>approved by: </h4>
                    <ul>
                        {props.approvers.map((name, i) => {
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
                    <h4>Bid has not been approved yet</h4>
                </>)}
        </div>
    )
}

export default ShopBid