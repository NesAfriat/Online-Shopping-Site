import classes from "../../styling/item.module.css";
import Card from "../../styling/Card";

const Discount = (props) => {

    return (
        <>
            <Card>
                <div className={classes.content}>
                    <h1>Buyer ID: {props.visitorId} </h1>
                    <div>
                        <h3>Shop ID: {props.shopId} </h3>
                    </div>
                    <div>
                        <h3>Cost : {props.cost} </h3>
                    </div>
                    <div>
                        <h3>Delivery ID: {props.deliveryId} </h3>
                    </div>
                    <div>
                        <h3>Charge receipt : {props.chargeReceipt} </h3>
                    </div>
                    <div>
                        <h3>Products ids and quantities: </h3>
                        <ul>
                            {props.productsIdToQuantity.map((entry,i) =>{
                                return <li key={i}>
                                    <h4>product id: {entry.key}</h4>
                                    <h4>quantity: {entry.value}</h4>
                                    <h4>~~~~~~~~~~~~~~~~~~~~~</h4>

                                </li>
                            })}
                        </ul>
                    </div>
                    {props.errorOccurred !== null && <h3>Error occurred: {props.errorOccurred}</h3>}
                </div>
            </Card>
        </>
    );
};

export default Discount;
