import classes2 from "../../../../styling/list.module.css";
import Policy from "../../../BasicComponent/Policy";
import Card from "../../../../styling/Card";
import classes from "../../../../styling/item.module.css";

const ViewAllPolicies = (props) => {
    return (
        <>
            <h1 className={classes.content2}>Purchase policies:</h1>
            <ul className={classes2.list}>
                {props.policies.map((purchasePolicy, i) => {
                    return (
                        <li key={i}>
                            <Card>
                                <Policy
                                    Id={purchasePolicy.purchasePolicyId}
                                    description={purchasePolicy.conditionDescription}
                                ></Policy>
                            </Card>
                        </li>
                    );
                })}
            </ul>
            <></>
        </>
    );
};

export default ViewAllPolicies