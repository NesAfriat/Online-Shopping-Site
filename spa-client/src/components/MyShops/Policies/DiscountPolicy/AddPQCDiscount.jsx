import {useRef, useState} from "react";
import classes from "../../../../styling/addForm.module.css";
import {postAddPQCondition} from "../../../../api";

const AddPQCDiscount = (props) => {
    const discountIdRef = useRef();
    const productIdRef = useRef();
    const minQuantityRef = useRef();

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

        const enteredtype = typeValue;
        console.log(enteredtype);
        const enteredDiscountId = discountIdRef.current.value;
        const enteredProductId = productIdRef.current.value;
        const enteredMinQuantity = minQuantityRef.current.value;
        await postAddPQCondition(
            props.visitorId,
            props.shopId,
            enteredtype,
            enteredDiscountId,
            enteredProductId,
            enteredMinQuantity
        ).then((res) => {
            console.log(res);
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                alert("Succesfully added discount");
                props.setter(false);
            }
        });
    };

    return (
        <div>
            <form onSubmit={submitHandler}>
                <div className={classes.control}>
                    <h1 className={classes.h1}>Enter discount information:</h1>
                    <fieldset>
                        Choose PQC type:
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
                    <div className={classes.control}>
                        <label htmlFor="discountId">Discount ID: </label>
                        <input type="number" required id="discountId" ref={discountIdRef}/>
                    </div>
                    <div className={classes.control}>
                        <label htmlFor="productId">Product ID: </label>
                        <input type="number" required id="productId" ref={productIdRef}/>
                    </div>
                    <div className={classes.control}>
                        <label htmlFor="minQuantity">Minimum product amount: </label>
                        <input
                            type="number"
                            required
                            id="minQuantity"
                            ref={minQuantityRef}
                        />
                    </div>
                    <div>
                        <button className={classes.actions}>Add new discount</button>
                    </div>
                </div>
            </form>
        </div>
    );
};

export default AddPQCDiscount;
