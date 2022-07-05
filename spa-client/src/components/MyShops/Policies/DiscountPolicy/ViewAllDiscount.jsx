import classes2 from "../../../../styling/list.module.css";
import Card from "../../../../styling/Card";
import Discount from "../../../BasicComponent/Discount";
import classes from "../../../../styling/item.module.css";

const ViewAllDiscounts = (props) => {
    console.log(props.discounts)
    return (
        <>
            <h1 className={classes.content2}>Discount policies:</h1>
            <ul className={classes2.list}>
                {props.discounts.map((discount, i) => {
                    return (
                        <li key={i}>
                            <Card>
                                <Discount
                                    id={discount.discountId}
                                    lastValidDate={discount.lastValidDate}
                                    description={discount.discountType}
                                ></Discount>
                            </Card>
                        </li>
                    );
                })}
            </ul>
            <></>
        </>
    );
};

export default ViewAllDiscounts