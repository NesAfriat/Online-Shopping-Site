import {useRef} from "react";
import classes from "../styling/addForm.module.css";
import {postRegister} from "../api";

const Register = (props) => {
    const usernameRef = useRef();
    const passwordRef = useRef();
    const emailRef = useRef();

    const submitHandler = async (e) => {
        e.preventDefault();

        const username = usernameRef.current.value;
        const password = passwordRef.current.value;
        const email = emailRef.current.value;

        await postRegister(props.visitorId, username, password, email).then(
            (res) => {
                if (res.errorOccurred) {
                    alert(res.errorMessage);
                    console.log(res.errorMessage);
                } else {
                    alert("Successfully registered");
                    props.setter(false);
                }
            }
        );

        usernameRef.current.value = "";
        passwordRef.current.value = "";
        emailRef.current.value = "";
    };

    return (
        <div>
            <form onSubmit={submitHandler}>
                <div className={classes.control}>
                    <h1 className={classes.h1}>Apply your information:</h1>
                    <label htmlFor="username">Username: </label>
                    <input type="text" required id="username" ref={usernameRef}/>
                </div>
                <div className={classes.control}>
                    <label htmlFor="password">Password: </label>
                    <input type="text" required id="password" ref={passwordRef}/>
                </div>
                <div className={classes.control}>
                    <label htmlFor="email">Email: </label>
                    <input type="text" required id="email" ref={emailRef}/>
                </div>
                <div>
                    <button className={classes.actions}>Sign up</button>
                </div>
            </form>
            {/*<button className={classes.returnButton} onClick={cancelHandler}>*/}
            {/*    Return*/}
            {/*</button>*/}
        </div>
    );
};

export default Register;
