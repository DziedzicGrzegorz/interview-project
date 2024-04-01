import {FieldError} from "react-hook-form";
import {FC} from "react";

interface ErrorMessageProps {
    error?: FieldError;
}

export const ErrorMessage: FC<ErrorMessageProps> = ({error}) => {
    return <>{error && <span className="text-red-500 text-sm">{error.message}</span>}</>;
};