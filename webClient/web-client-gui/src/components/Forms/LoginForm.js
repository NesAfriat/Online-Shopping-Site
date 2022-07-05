import { useRef } from "react";
import classes from "../addForm.module.css";
import { useHistory } from "react-router-dom";
import { postLogin } from "../../api";
import visitorId from "../VisitorId";

function LoginForm(props) {
  const history = useHistory();
  const usernameRef = useRef();
  const passwordRef = useRef();

  function failInput(res) {
    alert(res.errorMessage);
    history.replace("/login");
  }

  function Succes() {
    alert("The action completed succesfuly");
    history.replace("/market");
  }

  function cancelHandler() {
    console.log("clicked on Return");
    history.replace("/market");
  }

  const LoginMember = async (LoginData) => {
    const result = await postLogin(
      visitorId.id,
      LoginData.username,
      LoginData.password
    ).then((res) => {
      console.log(res);
      if (res.errorOccurred) {
        failInput(res);
      } else {
        Succes();
      }
      console.log(res);
      return res;
    });
    return result;
  };

  function submitHandler(event) {
    event.preventDefault();
    console.log("clicked on Login");
    const enteredUsername = usernameRef.current.value;
    const enteredPassword = passwordRef.current.value;

    const LoginData = {
      username: enteredUsername,
      password: enteredPassword,
    };

    usernameRef.current.value = "";
    passwordRef.current.value = "";
    LoginMember(LoginData);
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
      <button className={classes.returnButton} onClick={cancelHandler}>
        Return
      </button>
    </div>
  );
}

export default LoginForm;
