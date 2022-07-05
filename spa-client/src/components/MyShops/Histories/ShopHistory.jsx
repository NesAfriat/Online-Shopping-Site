import classes2 from "../../../styling/list.module.css";
import Card from "../../../styling/Card";
import classes from "../../../styling/item.module.css";
import PurchaseHistory from "../../BasicComponent/PurchaseHistory";

const ShopHistory = (props) => {
    console.log(props.history)
    return (<>
            <h1 className={classes.content2}>Shop Purchase history:</h1>
            <ul className={classes2.list}>
                {props.history.map((purchase, i) => {
                    return (
                        <li key={i}>
                            <Card>
                                <PurchaseHistory
                                    visitorId={purchase.visitorId}
                                    shopId={purchase.shopId}
                                    cost={purchase.cost}
                                    deliveryId={purchase.deliveryId}
                                    chargeReceipt={purchase.chargedReceipt}
                                    productsIdToQuantity={purchase.productIdToQuantity}
                                    errorOccurred={purchase.errorAcquired}
                                    errorMessage={purchase.purchaseError}
                                ></PurchaseHistory>
                            </Card>
                        </li>
                    );
                })}
            </ul>
            <></>
        </>
    );
};

export default ShopHistory;

