import classes from "../../styling/item.module.css";
import Card from "../../styling/Card";

const Discount = (props) => {
    return (
        <>
            <Card>
                <div className={classes.content}>
                    <h1>Discount ID : {props.id} </h1>
                    <div>
                        <h3>Description : {props.description} </h3>
                    </div>
                    <div>
                        <h3>Last valid date : {props.lastValidDate} </h3>
                    </div>
                </div>
            </Card>
        </>
    );
};

export default Discount;
