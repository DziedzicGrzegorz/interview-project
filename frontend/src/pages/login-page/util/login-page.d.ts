import {z} from "zod";
import {loginSchema, registerSchema} from "@/pages/login-page/util/login-pages.ts";

interface ErrorState {
    title: string;
    description: string;
}

type LoginFormFields = z.infer<typeof loginSchema>;
type RegisterFormFields = z.infer<typeof registerSchema>;
type FormFields = LoginFormFields | RegisterFormFields;