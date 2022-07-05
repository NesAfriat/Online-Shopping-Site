import {useRef} from "react";
import classes from "../styling/addForm.module.css";
import {postOpenShop} from "../api";

const OpenShop = (props) => {

    const nameRef = useRef();
    const phoneRef = useRef();
    const creditCardRef = useRef();
    const descRef = useRef();
    const locRef = useRef();

    const submitHandler = async (e) => {
        e.preventDefault();

        if (phoneRef.current.value === "" ||
            creditCardRef.current.value === "" ||
            nameRef.current.value === "" ||
            descRef.current.value === "" ||
            locRef.current.value === "") {
            alert("PLease fill in all fields")
            return
        }
        const phone = phoneRef.current.value;
        const card = creditCardRef.current.value;
        const name = nameRef.current.value;
        const desc = descRef.current.value;
        const loc = locRef.current.value;

        await postOpenShop(props.visitorId, phone, card, name, desc, loc).then(res => {
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                alert("Successfully opened shop!");
                props.setter(false);
            }
        })
    }

    return (
        <div>
            <form onSubmit={submitHandler}>
                <div className={classes.control}>
                    <h1 className={classes.h1}>Open shop:</h1>
                </div>
                <div className={classes.control}>
                    <label htmlFor="">phone number: </label>
                    <input type="text" required id="phone" ref={phoneRef} size="20"/>
                </div>
                <div className={classes.control}>
                    <label htmlFor="">credit card: </label>
                    <input
                        type="text"
                        required
                        id="credit card"
                        ref={creditCardRef}
                        size="20"
                    />
                </div>
                <div className={classes.control}>
                    <label htmlFor="">enter shop name: </label>
                    <input type="text" required id="shop name" ref={nameRef} size="20"/>
                </div>
                <div className={classes.control}>
                    <label htmlFor="">write shop description: </label>
                    <input
                        type="text"
                        required
                        id="shop destination"
                        ref={descRef}
                        size="20"
                    />
                </div>
                <div className={classes.control}>
                    <label htmlFor="">enter shop location: </label>
                    <input
                        type="text"
                        required
                        id="shop location"
                        ref={locRef}
                        size="20"
                    />
                </div>
                <div>
                    <button className={classes.actions}>open shop</button>
                </div>
            </form>
        </div>

    );
}
export default OpenShop;
