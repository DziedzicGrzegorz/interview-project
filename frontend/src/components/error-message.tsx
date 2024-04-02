import {FC} from "react";
import {FieldError} from "react-hook-form";

interface ErrorMessageProps {
    error?: FieldError;
}

//ErrorMessage is common component that can be used to display error messages in forms
export const ErrorMessage: FC<ErrorMessageProps> = ({error}) => {
    return <>{error && <span className="text-red-500 text-sm">{error.message}</span>}</>;
};