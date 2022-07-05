import { Link } from "react-router-dom";
import classes from "./mainNavigation.module.css";
function EnterNavigation() {
  return (
    <header className={classes.header}>
      <div className={classes.logo}>
        <nav>
          <ul>
            <li>
              <Link to="/login">
                <h3 className={classes.h3}>Login</h3>
              </Link>
            </li>
            <li>
              <Link to="/register">
                <h3 className={classes.h3}>Register</h3>
              </Link>
            </li>
            <li>
              <Link to="/market">
                <h3 className={classes.h3}>Continue as visitor</h3>
              </Link>
            </li>
          </ul>
        </nav>
      </div>
    </header>
  );
}

export default EnterNavigation;
