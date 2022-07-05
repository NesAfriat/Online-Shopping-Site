import {Dispatch, FC, FormEvent, SetStateAction, useRef} from "react";
// @ts-ignore
import classes from "../styling/addForm.module.css";
import {Cart} from "../models/Cart";
import {postLogin} from "../api";
import {AxiosResponse} from "axios";

const Login: FC<{
    visitorId: Number,
    setMember: (Dispatch<SetStateAction<String>>),
    setCart: (Dispatch<SetStateAction<Cart>>),
    setter: (Dispatch<SetStateAction<Boolean>>),
}> = (props) =>{
    const usernameRef = useRef<HTMLInputElement>();
    const passwordRef = useRef<HTMLInputElement>();

    const submitHandler = async (e:FormEvent) =>{
        e.preventDefault()
        if(usernameRef.current.value === "" || passwordRef.current.value === ""){
            alert("Please fill in all fields")
            return
        }
        const username = usernameRef.current!.value;
        const password = passwordRef.current!.value;

        await postLogin(props.visitorId, username, password)
            .then((res)=>{
                console.log(res)
                if(res.errorOccurred){
                    alert(res.errorMessage)
                }else{
                    alert("Successfully logged in!")
                    props.setMember(res.value.loggedIn.username)
                    props.setCart(JSON.parse(res.value.loggedIn.cart))
                    // props.setCart(res.value.loggedIn.shoppingCart.baskets) //TODO is doing troubles
                    props.setter(false);
                }
            })

        usernameRef.current!.value = "";
        passwordRef.current!.value = "";

    }

    return (
        <div>
            <form onSubmit={submitHandler}>
                <div className={classes.control}>
                    {" "}
                    <h1 className={classes.h1}>Enter userName and password:</h1>
                    <label htmlFor="username">UserName: </label>
                    <input type="text" required id="username" ref={usernameRef} />
                </div>
                <div className={classes.control}>
                    <label htmlFor="password">Password: </label>
                    <input type="password" required id="password" ref={passwordRef} />
                </div>
                <div>
                    <button className={classes.actions}>Sign in</button>
                </div>
            </form>
            {/*<button className={classes.returnButton} onClick={cancelHandler}>*/}
            {/*    Return*/}
            {/*</button>*/}
        </div>
    );
}
export default Login;