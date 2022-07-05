import classes from "../styling/addForm.module.css";
import {getIsAdmin, postLogin} from "../api";
import {useRef} from "react";

const Login = (props) => {
    const usernameRef = useRef();
    const passwordRef = useRef();

    const submitHandler = async (e) => {
        e.preventDefault();
        if (usernameRef.current.value === "" || passwordRef.current.value === "") {
            alert("Please fill in all fields");
            return;
        }
        const username = usernameRef.current.value;
        const password = passwordRef.current.value;
        props.socketProvider.connect(username);
        await postLogin(props.visitorId, username, password).then((res) => {
            console.log(res);
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage);
                props.socketProvider.disconnect()
            } else {
                props.setMember(res.value.loggedIn.username);
                props.setter(false);
            }
            return res.errorOccurred
        }).then(async (res) =>  {
            if(!res){
                await getIsAdmin(props.visitorId).then((res) => {
                    console.log(res);
                    if (res.errorOccurred) {
                        alert(res.errorMessage);
                        console.log(res.errorMessage);
                    } else {
                        console.log(res.value);
                        props.setIsAdmin(res.value);
                        if (res.value) {
                            alert("Welcome back Admin!");
                            props.socketProvider.adminConnect();
                        } else {
                            alert("Successfully logged in!");
                        }
                    }
                });
            }
        })
    };

    return (
        <div>
            <form onSubmit={submitHandler}>
                <div className={classes.control}>
                    {" "}
                    <h1 className={classes.h1}>Enter userName and password:</h1>
                    <label htmlFor="username">UserName: </label>
                    <input type="text" required id="username" ref={usernameRef}/>
                </div>
                <div className={classes.control}>
                    <label htmlFor="password">Password: </label>
                    <input type="password" required id="password" ref={passwordRef}/>
                </div>
                <div>
                    <button className={classes.actions}>Sign in</button>
                </div>
            </form>
        </div>
    );
};
export default Login;
