import {useRef, useState} from "react";
import classes from "../../../../styling/addForm.module.css";
import {postComposePurchasePolicies} from "../../../../api";

const AddCompose = (props) => {
    const firstPolicyRef = useRef();
    const secondPolicyRef = useRef();

    const useRadioButtons = (name) => {
        const [value, setState] = useState(null);
        const handleChange = (e) => {
            setState(e.target.value);
        };
        const inputProps = {
            name,
            type: "radio",
            onChange: handleChange,
        };
        return [value, inputProps];
    };
    const [typeValue, typeInputProps] = useRadioButtons("type");


    const submitHandler = async (e) => {
        e.preventDefault();
        const enteredfirstPolicy = firstPolicyRef.current.value;
        const enteredsecondPolicy = secondPolicyRef.current.value;

        await postComposePurchasePolicies(
            props.visitorId,
            props.shopId,
            typeValue,
            enteredfirstPolicy,
            enteredsecondPolicy
        ).then((res) => {
            console.log(res);
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                alert("Succesfully added policy");
                props.setter(false);
            }
        });
    };

    return (
        <div>
            <form onSubmit={submitHandler}>
                <div className={classes.control}>
                    <h1 className={classes.h1}>Enter policy information:</h1>
                    <fieldset>
                        Choose composition type:
                        <input
                            value="and"
                            checked={typeValue === "and"}
                            {...typeInputProps}
                        />{" "}
                        and
                        <input
                            value="or"
                            checked={typeValue === "or"}
                            {...typeInputProps}
                        />{" "}
                        or
                        <input
                            value="if"
                            checked={typeValue === "if"}
                            {...typeInputProps}
                        />{" "}
                        if
                        <input
                            value="reset"
                            checked={typeValue === "reset"}
                            {...typeInputProps}
                        />{" "}
                        reset
                    </fieldset>
                    <div>
                        <label htmlFor="firstPolicy">First policy ID: </label>
                        <input type="number" required id="first" ref={firstPolicyRef}/>
                    </div>
                    <div>
                        <label htmlFor="secondPolicy">Second policy ID: </label>
                        <input type="number" required id="secondPolicy" ref={secondPolicyRef}/>
                    </div>
                </div>
                <div>
                    <button className={classes.actions}>Add new policy</button>
                </div>
            </form>
        </div>
    );
};

export default AddCompose;
