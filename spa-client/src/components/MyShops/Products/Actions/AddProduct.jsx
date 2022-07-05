import classes from "../../../../styling/addForm.module.css";
import {postAddProduct} from "../../../../api";
import {useRef} from "react";

const AddProduct = (props) => {

    const nameRef = useRef();
    const quantityRef = useRef();
    const priceRef = useRef();
    const descRef = useRef();
    const catRef = useRef();


    const submitHandler = async (e) => {
        e.preventDefault();

        if (nameRef.current.value === "" ||
            quantityRef.current.value === "" ||
            priceRef.current.value === "" ||
            descRef.current.value === "" ||
            catRef.current.value === "") {
            alert("PLease fill in all fields")
            return
        }
        const name = nameRef.current.value;
        const quantity = quantityRef.current.value;
        const price = priceRef.current.value;
        const desc = descRef.current.value;
        const cat = catRef.current.value;

        await postAddProduct(props.visitorId, props.shopId, name, quantity, price, desc, cat).then(res => {
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                alert("Successfully added product!");
                props.reload(props.shopId);
                props.setter(false);
            }
        })

    }

    return (
        <div>
            <form onSubmit={submitHandler}>
                <div className={classes.control}>
                    <h1 className={classes.h1}>add product:</h1>
                </div>
                <div className={classes.control}>
                    <label htmlFor="">name: </label>
                    <input type="text" required id="name" ref={nameRef} size="20"/>
                </div>
                <div className={classes.control}>
                    <label htmlFor="">quantity: </label>
                    <input
                        type="text"
                        required
                        id="quantity"
                        ref={quantityRef}
                        size="20"
                    />
                </div>
                <div className={classes.control}>
                    <label htmlFor="">price: </label>
                    <input type="text" required id="price" ref={priceRef} size="20"/>
                </div>
                <div className={classes.control}>
                    <label htmlFor="">description: </label>
                    <input
                        type="text"
                        required
                        id="description"
                        ref={descRef}
                        size="20"
                    />
                </div>
                <div className={classes.control}>
                    <label htmlFor="">category: </label>
                    <input
                        type="text"
                        required
                        id="cat"
                        ref={catRef}
                        size="20"
                    />
                </div>
                <div>
                    <button className={classes.actions}>add product</button>
                </div>
            </form>
        </div>

    );
}

export default AddProduct