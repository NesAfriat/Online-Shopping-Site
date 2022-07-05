import { Link } from "react-router-dom";
import classes from "./mainNavigation.module.css";
function MainNavigation(props) {
  return (
    <div>
      {props.connected && (
        <header className={classes.header}>
          <div className={classes.logo}>
            <nav>
              <ul>
                <li>
                  <Link to="/enter-system" onClick={props.click}>
                    <h3 className={classes.h3}>Visit our site!</h3>
                  </Link>
                </li>
              </ul>
            </nav>
          </div>
        </header>
      )}
    </div>
  );
}

export default MainNavigation;
