import classes from "../../styling/item.module.css";
import Card from "../../styling/Card";
import {getPermissionsOfMember, postRemoveShopOwner} from "../../api";
import {useState} from "react";

const Role = (props) => {
    const [permissions, setPermissions] = useState([]);
    const [viewPermissions, setViewPermissions] = useState(false);

    const removeOwner = async (visitorId, member, shopId) => {
        console.log(`clicked on removeOwner`);
        await postRemoveShopOwner(visitorId, member, shopId).then((res) => {
            // console.log(res);
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage);
            } else {
                props.reload();
                alert("Shop owner remove succesfully");
            }
        });
    };

    // const removeManager = async (visitorId, member, shopId) => {
    //     alert("not implemented remove manager");
    //     console.log(`clicked on removeManager`);
    //     await postRemoveShopOwner(visitorId, member, shopId).then((res) => {
    //         if (res.errorOccurred) {
    //             alert(res.errorMessage);
    //             return;
    //         } else {
    //             alert("Shop manager remove succesfully");
    //         }
    //     });
    // };

    const loadRolePermissions = async () => {
        await getPermissionsOfMember(
            props.visitorId,
            props.shopId,
            props.member
        ).then((res) => {
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage);
            } else {
                if (res.value.length === 0) {
                    alert("No permissions found");
                    return;
                }
                setPermissions([...res.value]);
            }
        });
    };
    const viewAllPermissions = () => {
        loadRolePermissions();
        setViewPermissions(true);
    };

    // const showMemberPermissions = () => {
    //     alert("not implemented permissions in domain");
    //     return <>nothing</>;
    // };

    return (
        <>
            <Card>
                <div className={classes.content}>
                    <h1>Member : {props.member} </h1>
                    <div>
                        {props.founder === props.member ? (
                            <h3>Founder</h3>
                        ) : (
                            <h3>Assigned by {props.assignor}</h3>
                        )}
                    </div>
                    <div>
                        <ul>
                            <li>
                                <button
                                    onClick={() =>
                                        removeOwner(props.visitorId, props.member, props.shopId)
                                    }
                                >
                                    Remove owner
                                </button>
                            </li>
                            <li>
                                <button onClick={viewAllPermissions}>Permissions</button>
                            </li>
                        </ul>
                        {viewPermissions && (
                            <showMemberPermissions permissions={permissions}/>
                        )}
                    </div>
                </div>
            </Card>
        </>
    );
};

export default Role;
