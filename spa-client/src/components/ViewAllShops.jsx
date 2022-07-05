import {useState} from "react";
import classes2 from "../styling/item.module.css";
import Card from "../styling/Card";
import classes from "../styling/list.module.css";
import GeneralShop from "./BasicComponent/GeneralShop";

const ViewAllShops = (props) => {
    const [showShop, setShowShop] = useState(null);
    const updateShop = (shop) => {
        setShowShop(shop);
        console.log(showShop);
    };

    return (
        <div>
            <h1 className={classes2.content2}>Open for business:</h1>
            <ul className={classes.list}>
                {props.shops.map((shop, i) => {
                    return (
                        <li key={i}>
                            <button className={classes.actions} onClick={() => updateShop(shop)}>
                                <Card>
                                    <div className={classes2.content}>
                                        <h1>{shop.name}'s shop</h1>
                                        <h3>{shop.description}</h3>
                                    </div>
                                </Card>
                            </button>
                        </li>
                    );
                })}
            </ul>
            <>
                {showShop !== null && (
                    <GeneralShop
                        visitorId={props.visitorId}
                        id={showShop.id}
                        name={showShop.name}
                        founder={showShop.founder}
                        location={showShop.location}
                        description={showShop.description}
                        products={showShop.products}
                        isAdmin={props.isAdmin}
                        member={props.member}
                    />
                )}
            </>
        </div>
    );
};
export default ViewAllShops;
