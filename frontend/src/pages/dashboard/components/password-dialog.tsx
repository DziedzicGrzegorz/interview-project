import {Dispatch, SetStateAction} from "react";
import {FieldValues, SubmitHandler, useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {z} from "zod";

import {
    Dialog,
    DialogContent,
    DialogFooter,
} from "@/components/ui/dialog";
import {CardHeader, CardContent} from "@/components/ui/card";
import {TextGenerateEffect} from "@/components/text-generator-effect";
import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button/button";
import {ErrorMessage} from "@/components/error-message";

interface PasswordDialogProps {
    dialogOpen: boolean;
    setOpenDialog: (open: boolean) => void;
    descriptionToCipher?: string;
    setPassword: Dispatch<SetStateAction<string>>;
}

const passwordSchema = z.object({
    password: z.string().min(6, {message: "Password must be at least 6 characters long"}).max(140, {message: "Password must be at most 140 characters long"}),
});
type PasswordSchema = z.infer<typeof passwordSchema>;

export function PasswordDialog({
                                   dialogOpen,
                                   setOpenDialog,
                                   descriptionToCipher,
                                   setPassword,
                               }: PasswordDialogProps) {
    const {
        register,
        handleSubmit,
        formState: {errors},
        reset,
    } = useForm<PasswordSchema>({
        resolver: zodResolver(passwordSchema),
    });

    const onSubmit: SubmitHandler<FieldValues> = (formData) => {

        setPassword(formData.password);

        setTimeout(() => {
            setOpenDialog(false);
            reset();
        }, 500);
    };

    return (
        <Dialog open={dialogOpen} onOpenChange={setOpenDialog}>
            <DialogContent className="sm:max-w-[425px]">
                <form onSubmit={handleSubmit(onSubmit)}>
                    <CardHeader>
                        <h3 className="text-xl font-medium leading-6 text-gray-900">
                            <TextGenerateEffect words={"To encrypt this message, please provide a password."}/>
                        </h3>
                    </CardHeader>
                    <CardContent className="space-y-4">
                        <div className="max-w-[300px]">
                            <span className="text-xl text-gray-500">
                                Message ID
                            </span>
                            <br/>
                            <span className="whitespace-normal break-words">
                                {descriptionToCipher}
                            </span>
                        </div>
                        <div>
                            <Input id="password" type="password" {...register('password')} />
                            <ErrorMessage error={errors.password}/>
                        </div>
                    </CardContent>

                    <DialogFooter>
                        <Button type="submit">Decrypt</Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    );
}
