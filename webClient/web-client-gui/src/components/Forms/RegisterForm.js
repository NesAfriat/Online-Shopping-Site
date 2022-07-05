import { useRef } from "react";
import classes from "../addForm.module.css";
import { useHistory } from "react-router-dom";
import { postRegister } from "../../api";
import visitorId from "../VisitorId";

function RegisterForm(props) {
  const history = useHistory();
  const usernameRef = useRef();
  const passwordRef = useRef();
  const emailRef = useRef();

  function failInput(errorMessage) {
    alert(errorMessage);
    history.replace("/register");
  }
  function Succes() {
    alert("The action completed succesfuly");
    history.replace("/market");
  }

  const RegisterMember = async (registerData) => {
    const result = await postRegister(
      visitorId.id,
      registerData.username,
      registerData.password,
      registerData.email
    ).then((res) => {
      console.log(res);
      if (res.errorOccured) {
        failInput(res);
      } else {
        Succes();
      }
      return res;
    });
    return result;
  };

  function cancelHandler() {
    history.replace("/market");
  }
  function submitHandler(event) {
    event.preventDefault();
    const enteredUsername = usernameRef.current.value;
    const enteredPassword = passwordRef.current.value;
    const enteredEmail = emailRef.current.value;

    const registerData = {
      username: enteredUsername,
      password: enteredPassword,
      email: enteredEmail,
    };

    usernameRef.current.value = "";
    passwordRef.current.value = "";
    emailRef.current.value = "";
    RegisterMember(registerData);
  }

  return (
    <div>
      <form onSubmit={submitHandler}>
        <div className={classes.control}>
          <h1 className={classes.h1}>Apply your information:</h1>
          <label htmlFor="username">Username: </label>
          <input type="text" required id="username" ref={usernameRef} />
        </div>
        <div className={classes.control}>
          <label htmlFor="password">Password: </label>
          <input type="text" required id="password" ref={passwordRef} />
        </div>
        <div className={classes.control}>
          <label htmlFor="email">Email: </label>
          <input type="text" required id="email" ref={emailRef} />
        </div>
        <div>
          <button className={classes.actions}>Sign up</button>
        </div>
      </form>
      <button className={classes.returnButton} onClick={cancelHandler}>
        Return
      </button>
    </div>
  );
}

export default RegisterForm;
