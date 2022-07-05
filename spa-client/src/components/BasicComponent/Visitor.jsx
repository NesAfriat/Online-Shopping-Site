import classes from "../../styling/item.module.css";
import Card from "../../styling/Card";

const Visitor = (props) => {
    return (
        <>
            <Card>
                <div className={classes.content}>
                    <h3>Visitor ID: {props.id} </h3>
                    {props.logged && <h3>Logged in</h3>}
                </div>
            </Card>
        </>
    );
};

export default Visitor;
