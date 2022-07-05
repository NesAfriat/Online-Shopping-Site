import classes from "../../styling/item.module.css";
import Card from "../../styling/Card";

const Policy = (props) => {

    return (
        <>
            <Card>
                <div className={classes.content}>
                    <h1>Policy ID : {props.Id} </h1>
                    <div>
                        <h3>Description : {props.description} </h3>
                    </div>
                </div>
            </Card>
        </>
    );
};

export default Policy;
